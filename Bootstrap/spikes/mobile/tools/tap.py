"""Renvoie le centre "x y" du premier nœud d'un dump uiautomator dont le texte ou le content-desc vaut le label donné."""
import re, sys
xml = open(sys.argv[1]).read(); label = sys.argv[2]
for m in re.finditer(r'<node [^>]*>', xml):
    n = m.group(0)
    if f'content-desc="{label}"' in n or f'text="{label}"' in n:
        x1, y1, x2, y2 = map(int, re.search(r'bounds="\[(\d+),(\d+)\]\[(\d+),(\d+)\]"', n).groups())
        print((x1 + x2) // 2, (y1 + y2) // 2); break
