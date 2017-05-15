#! /usr/bin/env python3
from operator import itemgetter
import sys
import matplotlib.pyplot as plt
from itertools import groupby
import pprint


def read_and_parse_file(filename):
    with open(input_filename) as file:
        next(file) 
        split_lines = [x.split() for x in file.readlines()]
        return [ [line[0], float(line[1]), float(line[4].replace(',', '.'))] for line in split_lines]

if (len(sys.argv) != 3):
    print("Usage: %s <results file> <lin|log>" % (sys.argv[0]))
    exit(1)

input_filename = sys.argv[1]
print("Opening file %s" % (input_filename))

data = read_and_parse_file(input_filename)

groups = [list(g) for k, g in groupby(data, itemgetter(0))]

groups_transposed = []

for group in groups:
    print(group)
    name = group[0][0]
    array_x = []
    array_y = []
    for point in group:
        array_x.append(point[1])
        array_y.append(point[2])
    groups_transposed.append([array_x, array_y, name])




print(groups_transposed)

if (sys.argv[2] == 'log'):
    plt.yscale('log')

for series in groups_transposed:
    plt.plot(series[0], series[1], linestyle='-', marker='o', label=series[2])
    plt.legend(prop={'size': 8})

plt.ylabel('ops/s')
plt.xlabel('Dataset size')
plt.title('Plot')
plt.show()
