# AI Usage — Skillforge

## Tools used
- **Claude** — initial project scaffolding (Gradle setup, data models, navigation, theme, Compose screen skeletons) and debugging the `libs.versions.toml` / Compose plugin issue.
- **Google Antigravity** — UI matching against the reference screens, fixing category card sizing, locked-lesson logic, and the real video player + loading/error states.
- **ChatGPT** — quick syntax/API lookups while wiring things up.
- **Gemini (Android Studio plugin)** — inline code completion and small refactors while editing.

## Prompts 

**Claude — kicking off the project**
> "this is an android assessment...everything is mentioned in the link [...] I need you to assist me with the assignment and help me complete it. give me step by step guide as well as code to start with project and rest details you can read on the attached link (use xml layouts or jetpack compose, follow the instructions on the link, so you help me figure out what can we do as per your understanding as i am comfortable with any option)"

**Claude — debugging the build**
> "there is some issue with the lib.versions.toml: [pasted `libs.versions.toml` and `build.gradle.kts`]"

**Antigravity — UI polish + real requirements pass**
> "on HomeScreen all category cards are not of equal sizes...make sure each of has same height. on CourseDetailScreen the course content which are not free should not be able clicked and moved forward. and on LessonScreen instead of applying fake UI, you can parse the link from json and applying loading progressbar as the requirements says, we have a actual video player and also fullscreen must convert the ui to full screen. Also handle the basics: a loading spinner while data loads, and a short error message if the request fails. Load images from their URLs with Coil or Glide. rest everything is top notch and dont change any other code...update only the ones required as per the changes i have mentioned above"

## What AI got right
Antigravity correctly spotted and removed a conflicting plugin declaration in the build file (`kotlin.android` plugin) that was causing the Gradle setup to misbehave alongside the Compose compiler plugin — a fix I wouldn't have caught quickly on my own.

## What AI got wrong (and the fix)
No single prompt produced flat-out wrong code, but the AI tools didn't fully resolve a build configuration issue on the first pass — the `libs.versions.toml` was missing the Kotlin/Compose plugin entries entirely (it had been generated for a plain Views template and never updated when the project switched to Compose). I ended up tracing the actual Gradle error myself, confirmed which plugin entries were missing, and added them manually rather than relying solely on AI suggestions.
