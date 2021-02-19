from os import write
import sys
from googletrans import Translator

def update_progress(complete, total, failures):
    percentage = complete / total
    progress_possibilities = [
        "[>                              ]",
        "[=>                             ]",
        "[==>                            ]",
        "[===>                           ]",
        "[====>                          ]",
        "[=====>                         ]",
        "[======>                        ]",
        "[=======>                       ]",
        "[========>                      ]",
        "[=========>                     ]",
        "[==========>                    ]",
        "[===========>                   ]",
        "[============>                  ]",
        "[=============>                 ]",
        "[==============>                ]",
        "[===============>               ]",
        "[================>              ]",
        "[=================>             ]",
        "[==================>            ]",
        "[===================>           ]",
        "[====================>          ]",
        "[=====================>         ]",
        "[======================>        ]",
        "[=======================>       ]",
        "[========================>      ]",
        "[=========================>     ]",
        "[==========================>    ]",
        "[===========================>   ]",
        "[============================>  ]",
        "[=============================> ]",
        "[===============================]",
    ]
    percentage_resolution = 1 / len(progress_possibilities)
    resolutions = [x * percentage_resolution for x in range(1, len(progress_possibilities) + 1)]
    prev_resolution = 0
    count = 0
    index = 0
    for val in resolutions:
        if percentage >= prev_resolution and percentage < val:
            index = count
            break
        count += 1
    print(f"{complete}/{total}:" + progress_possibilities[index] + f" {failures} failed.", end="\r")

gt = Translator()
translation_count = 0

params = sys.argv
src_path = params[1]
dest_path = params[2]

# filepath = "./processed_datasets/sentiment_treebank/tri_train_x.txt"
file = open(src_path, "r")
out_file = open(dest_path, "w+")

n_lines = len(open(src_path, "r").readlines())
count = 0
failures = 0
spun = set()

for line in file.readlines():
    update_progress(count, n_lines, failures)
    try:
        de = gt.translate(line, src='en', dest='de')
        fr = gt.translate(line, src='en', dest='fr')
        en1 = gt.translate(de.text, src='de', dest='en')
        en2 = gt.translate(fr.text, src='fr', dest='en')
    except:
        count += 1
        failures += 1
        continue
    spun.add(en1.text.lower())
    spun.add(en2.text.lower())
    count += 1

for line in spun:
    out_file.write(f"{line}\n")

