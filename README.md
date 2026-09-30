# CytoGen Panel Designer (GoCyto)

A Java Swing desktop application that designs multicolor flow cytometry antibody panels.
Tell it your cytometer, your inventory of conjugated antibodies, and the markers you want
to detect — it assigns fluorochromes to detectors using **simulated annealing**, matching
dye brightness against antigen density.

## Background

In a flow cytometry experiment, cells are stained with antibodies, each tagged with a
fluorescent dye ("fluorochrome", e.g. PE, FITC, APC). A cytometer has a fixed set of
lasers and detectors (channels). The hard part of experiment design is deciding **which
dye goes on which antibody for which cell-surface marker, on which detector** — a "panel".

This tool automates that assignment. You define:

- **Flow cytometer** — lasers (e.g. Blue 488 nm, Red 633 nm, Violet 405 nm) and their
  detectors/channels (e.g. `780/60`), plus which conjugates each detector can read.
- **Antibody database** — your inventory: target (e.g. `CD4`), conjugate (e.g. `BV421`),
  species, vendor, catalog/clone/isotype, amount per sample. Importable from CSV or
  Excel (`.xls`).
- **Target set** — the markers you want in the experiment, each with an **antigen
  density** (1 = dimmest … 10 = brightest) and an optional preassigned conjugate
  (or "Auto Select" to let the solver choose). Targets can be grouped, including a
  **required group** that must appear in every panel of the solution.

The solver then computes one or more panels — assignments of targets to detectors —
optimized so that **bright dyes land on dim markers and dim dyes on abundant markers**
(brightness + density ≈ 10 per assignment), penalizing panel-to-panel variance and,
optionally, the number of panels.

> Note: the optimizer matches brightness against antigen density and assignment
> validity. It does **not** model spectral spillover or compensation.

## Screenshots / UI

There are two UI generations in the codebase:

**GoCyto wizard** (`ui.nextgen.GoCytoFrame`) — the newer step-by-step UI:
1. Choose a working directory; set up or load a flow cytometer (detector tables)
2. Set up the antibody database (load/import, add/delete rows)
3. Build target testsets (targets, densities, preassigned conjugates, groups)
4. Run: pick cytometer + antibody database + target testset, then **Run**
5. Browse panel solutions (selector, results table, previous/next panel)

**Classic UI** (`ui.MainFrame`, "Antibody Panel Designer"):
- *Antibody Database Setup* — manage antibody databases (CSV/Excel import)
- *Antibody Panel Design* — pick cytometer configuration + database, define targets and
  annealing parameters (initial-temperature and cooling-rate sliders), run the solver
  with a progress bar, browse solutions and **export results to Excel**

Supporting dialogs let you tune conjugate brightness tables (with slider editors),
design detector→conjugate configurations, and view help/about.

## Project structure

```
src/com/lansoft/flowcytometry/
├── model/        JAXB-annotated domain objects + XML persistence (ModelDAO)
│                 Antibody, Conjugate, Detector, Laser, FlowCytometer,
│                 Configuration, TargetDefinition, TargetGroup, ...
├── service/      FlowCytometrySolver (simulated annealing), Group bitmask utils
├── ui/           Classic Swing UI (MainFrame + dialogs)
└── ui/nextgen/   GoCyto wizard UI
configuration/    Seed XML data: FlowCytometers.xml, Configurations.xml,
                  Conjugates.xml, AntibodyDatabases.xml (FortessaAB sample)
resource/         Application icons
executable/       Prebuilt 2014 jar + Windows launchers (see caveats)
TestAb_list.csv / .xlsx / biggerdb.xls   Sample antibody databases
```

**Runtime data:** on first launch the app reads/writes `*.xml` files in a
`flowcytometry/` folder under your home directory (e.g. `~/flowcytometry/`).
Seed it from `configuration/` to start with the sample cytometers and databases.

## Getting started

**Requirements**

- JDK 8 or earlier recommended. The code targets Java 6 and uses `javax.xml.bind`
  (JAXB), which was removed from the JDK in Java 11 — on newer JDKs add JAXB
  dependencies yourself.
- All third-party jars are bundled at the repo root: `weblaf-1.27.jar`,
  `seaglasslookandfeel-0.2.jar`, `guava-17.0.jar`, `jxl.jar`, `forms-1.3.0.jar`,
  `concurrent-trees-2.3.0.jar`.

**Build**

NetBeans Ant project (`build.xml` → `nbproject/build-impl.xml`), also importable
into Eclipse via `.project`/`.classpath` (the Eclipse classpath is the more
complete one).

**Run**

> `nbproject/project.properties` declares `main.class=com.lansoft.flowcytometry.FlowCytometryApp`,
> but that class does not exist — `ant run` will fail. Launch an entry point directly:

```sh
# GoCyto wizard (recommended)
java -cp ".:forms-1.3.0.jar:jxl.jar:guava-17.0.jar:seaglasslookandfeel-0.2.jar:weblaf-1.27.jar:concurrent-trees-2.3.0.jar:resource" \
  com.lansoft.flowcytometry.ui.nextgen.GoCytoFrame

# Classic UI
java -cp ".:forms-1.3.0.jar:jxl.jar:guava-17.0.jar:seaglasslookandfeel-0.2.jar:weblaf-1.27.jar:concurrent-trees-2.3.0.jar:resource" \
  com.lansoft.flowcytometry.ui.MainFrame
```

(On Windows use `;` as the classpath separator.)

## The algorithm

`service.FlowCytometrySolver` implements simulated annealing over panel assignments:

- A solution assigns one target per detector slot across N panels; neighbor moves swap assignments.
- The energy function rewards fluorochrome brightness + antigen density summing to ~10
  per assignment, multiplies penalties for group/validity violations, and adds a
  variance term across the panel plus optional panel-count penalization.
- Geometric cooling (`temp *= 1 - coolingRate`) until `temp <= 1`, with textbook
  Metropolis acceptance; a temperature-change listener drives the UI progress bar.
- On failure it reheats up to 20 times before throwing `CalculationException`.

Tunable from the UI: initial temperature, cooling rate, queue size, and panel-count
penalization. Larger/harder panels generally need a higher initial temperature.

## Caveats

- The GoCyto wizard's `.xlsx` import (`nextgen.ExcelReader`) needs Apache POI, which
  is **not** bundled — use CSV or `.xls` import (classic UI) instead.
- The sample `Conjugates.xml` in `configuration/` is wrapped in an XML comment and
  therefore inactive; brightness values are editable in the UI.
- `executable/flow-cytometry.jar` (2014) was built from an older package layout and
  does not match the current sources.
- `seaglasslookandfeel-0.2.jar` is on the classpath but not actively used.

## History

Originally developed at Lansoft; the `nextgen` GoCyto wizard UI is a later addition.
Sources date to 2017. Migrated from Bitbucket to GitHub in 2026.
