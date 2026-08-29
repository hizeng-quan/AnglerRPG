
with open("core/src/main/java/com/fishingrpg/game/screens/CatalogScreen.java", "r", encoding="utf-8") as f:
    lines = f.readlines()

header_rects = []
start_hr = -1
for i, l in enumerate(lines):
    if "// Header Background" in l: start_hr = i
    if "// Draw Collections" in l:
        end_hr = i
        header_rects = lines[start_hr:end_hr]
        break

header_texts = []
start_ht = -1
for i, l in enumerate(lines):
    if "// Title Centered" in l: start_ht = i
    if "for (int i = 0; i < collections.size(); i++) {" in l and start_ht != -1:
        end_ht = i
        header_texts = lines[start_ht:end_ht]
        break

new_lines = []
skip = False
for i, l in enumerate(lines):
    if "// Header Background" in l: skip = True
    if "// Draw Collections" in l: skip = False
    
    if "// Title Centered" in l: skip = True
    if "for (int i = 0; i < collections.size(); i++) {" in l and skip: skip = False
    
    if not skip:
        new_lines.append(l)

out_lines = []
for l in new_lines:
    if "// Draw tooltip" in l:
        out_lines.extend([
            "        Gdx.gl.glEnable(GL20.GL_BLEND);\n",
            "        sr.begin(ShapeRenderer.ShapeType.Filled);\n"
        ])
        out_lines.extend(header_rects)
        out_lines.append("        sr.end();\n")
        out_lines.append("        game.batch.begin();\n")
        out_lines.extend(header_texts)
        out_lines.append("        game.batch.end();\n\n")
        
    out_lines.append(l)

content = "".join(out_lines)
content = content.replace("\"QUAY L?I\", 25, H - 30", "\"QUAY L?I\", 32, H - 30")

with open("core/src/main/java/com/fishingrpg/game/screens/CatalogScreen.java", "w", encoding="utf-8") as f:
    f.write(content)

