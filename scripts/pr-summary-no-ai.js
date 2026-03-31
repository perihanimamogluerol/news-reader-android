import fs from "node:fs/promises";

const GITHUB_TOKEN = process.env.GITHUB_TOKEN;
const REPO = process.env.REPO;

if (!GITHUB_TOKEN) {
  throw new Error("GITHUB_TOKEN is missing");
}

if (!REPO) {
  throw new Error("REPO is missing");
}

async function github(path) {
  const response = await fetch(`https://api.github.com${path}`, {
    headers: {
      Authorization: `Bearer ${GITHUB_TOKEN}`,
      Accept: "application/vnd.github+json",
      "X-GitHub-Api-Version": "2022-11-28"
    }
  });

  if (!response.ok) {
    const text = await response.text();
    throw new Error(`GitHub API error ${response.status}: ${text}`);
  }

  return response.json();
}

function getSinceDate(hoursAgo = 24) {
  const now = new Date();
  return new Date(now.getTime() - hoursAgo * 60 * 60 * 1000);
}

async function getRecentlyMergedPrs() {
  const pulls = await github(`/repos/${REPO}/pulls?state=closed&per_page=100`);

  const since = getSinceDate(24);

  return pulls.filter((pr) => {
    return pr.merged_at && new Date(pr.merged_at) >= since;
  });
}

async function getPrFiles(prNumber) {
  return github(`/repos/${REPO}/pulls/${prNumber}/files?per_page=100`);
}

async function getPrCommits(prNumber) {
  return github(`/repos/${REPO}/pulls/${prNumber}/commits?per_page=100`);
}

async function enrichPr(pr) {
  const [files, commits] = await Promise.all([
    getPrFiles(pr.number),
    getPrCommits(pr.number)
  ]);

  return {
    number: pr.number,
    title: pr.title,
    body: pr.body || "",
    author: pr.user?.login || "unknown",
    mergedAt: pr.merged_at,
    url: pr.html_url,
    labels: (pr.labels || []).map((l) => l.name),
    files: files.map((f) => ({
      filename: f.filename,
      status: f.status
    })),
    commitMessages: commits
      .map((c) => c.commit?.message?.split("\n")[0])
      .filter(Boolean)
  };
}

function analyzePr(pr) {
  const layers = new Set();
  const modules = new Set();

  let risk = "low";
  let hasTests = false;
  let hasBuildChange = false;
  let hasNavigationChange = false;

  for (const file of pr.files) {
    const path = file.filename.toLowerCase();

    if (path.includes("viewmodel")) layers.add("ViewModel");
    if (path.includes("screen") || path.includes("/ui/") || path.includes("compose")) {
      layers.add("UI");
    }
    if (path.includes("repository") || path.includes("/data/") || path.includes("/network/")) {
      layers.add("Data");
    }
    if (path.includes("usecase") || path.includes("/domain/")) {
      layers.add("Domain");
    }
    if (path.includes("test")) {
      layers.add("Tests");
      hasTests = true;
    }

    if (path.startsWith("feature/")) {
      modules.add(path.split("/")[1] || "unknown");
    }
    if (path.startsWith("app/")) {
      modules.add("app");
    }

    if (path.includes("navigation")) {
      hasNavigationChange = true;
      risk = "medium";
    }

    if (
      path.endsWith("build.gradle") ||
      path.endsWith("build.gradle.kts") ||
      path.includes("gradle/")
    ) {
      hasBuildChange = true;
      risk = "high";
    }
  }

  const changeType = detectChangeType(pr);

  return {
    layers: Array.from(layers),
    modules: Array.from(modules),
    risk,
    hasTests,
    hasBuildChange,
    hasNavigationChange,
    changeType
  };
}

function detectChangeType(pr) {
  const text = `${pr.title}\n${pr.commitMessages.join("\n")}`.toLowerCase();

  if (text.includes("fix")) return "bugfix";
  if (text.includes("refactor")) return "refactor";
  if (text.includes("test")) return "test";
  if (text.includes("feat") || text.includes("feature")) return "feature";

  return "other";
}

function buildMarkdown(prs) {
  let md = `# Daily PR Summary\n\n`;
  md += `Generated at: ${new Date().toISOString()}\n\n`;

  if (prs.length === 0) {
    md += `No pull requests were merged in the last 24 hours.\n`;
    return md;
  }

  for (const pr of prs) {
    md += `## PR #${pr.number} — ${pr.title}\n\n`;
    md += `- Author: ${pr.author}\n`;
    md += `- Merged at: ${pr.mergedAt}\n`;
    md += `- URL: ${pr.url}\n`;
    md += `- Type: ${pr.analysis.changeType}\n`;
    md += `- Risk: ${pr.analysis.risk}\n`;
    md += `- Layers: ${pr.analysis.layers.join(", ") || "None"}\n`;
    md += `- Modules: ${pr.analysis.modules.join(", ") || "None"}\n`;
    md += `- Has tests: ${pr.analysis.hasTests ? "Yes" : "No"}\n`;
    md += `- Build config change: ${pr.analysis.hasBuildChange ? "Yes" : "No"}\n`;
    md += `- Navigation change: ${pr.analysis.hasNavigationChange ? "Yes" : "No"}\n`;

    if (pr.labels.length > 0) {
      md += `- Labels: ${pr.labels.join(", ")}\n`;
    }

    md += `- Changed files (${pr.files.length}):\n`;
    for (const file of pr.files.slice(0, 10)) {
      md += `  - ${file.filename} (${file.status})\n`;
    }

    if (pr.files.length > 10) {
      md += `  - ...and ${pr.files.length - 10} more\n`;
    }

    if (pr.commitMessages.length > 0) {
      md += `- Commit messages:\n`;
      for (const message of pr.commitMessages.slice(0, 5)) {
        md += `  - ${message}\n`;
      }
    }

    md += `\n`;
  }

  return md;
}

async function main() {
  console.log(`Running PR summary for repo: ${REPO}`);

  const prs = await getRecentlyMergedPrs();
  const enriched = await Promise.all(prs.map(enrichPr));

  const analyzed = enriched.map((pr) => ({
    ...pr,
    analysis: analyzePr(pr)
  }));

  const markdown = buildMarkdown(analyzed);

  console.log(markdown);
  await fs.writeFile("daily-pr-summary.md", markdown, "utf8");
}

main().catch((error) => {
  console.error(error);
  process.exit(1);
});
