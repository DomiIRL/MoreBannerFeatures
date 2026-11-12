# Prints the stonecutter nodes from settings.gradle.kts as a JSON list, e.g. ["26.3-fabric", ...],
# or with a loader argument only that loader's nodes, space-separated.
import json, re, sys

settings = open("settings.gradle.kts").read()
nodes = [f"{version}-{loader}"
         for version, loaders in re.findall(r'^\s*mc\("([^"]+)",\s*(.+)\)\s*$', settings, re.M)
         for loader in re.findall(r'"([^"]+)"', loaders)]
assert nodes, "no mc(...) nodes found in settings.gradle.kts"
if len(sys.argv) > 1:
    print(" ".join(n for n in nodes if n.endswith("-" + sys.argv[1])))
else:
    print(json.dumps(nodes))
