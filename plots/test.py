#! /usr/bin/env python3
from operator import itemgetter
import sys
import matplotlib.pyplot as plt
from itertools import groupby
import pprint

if (len(sys.argv) != 2):
    print("Usage: %s <results file>" % (sys.argv[0]))
    exit(1)

input_filename = sys.argv[1]
print("Opening file %s" % (input_filename))

with open(input_filename) as file:
    next(file)
    data = [ [line[0], int(line[1]), float(line[4].replace(',', '.'))] for line in [x.split() for x in file.readlines()]]

groups = []
for k, g in groupby(data, itemgetter(0)):
   groups.append(list(g))    # Store group iterator as a list

groups_without_labels = []
for group in groups:
    groups_without_labels.append([[x[1], x[2]] for x in group])


groups_transposed = [list(map(list, zip(*l))) for l in groups_without_labels]

plt.yscale('log')
for series in groups_transposed:
    plt.plot(series[0], series[1], linestyle='-', marker='o')

plt.ylabel('Results')
plt.show()
