#! /bin/bash

# For some reason 'dbgen' hangs on some scale factors when ran directly.
# This seems to fix it.
echo "Starting dbgen wrapper script with arguments: $@"
./dbgen "$@" >/dev/null 2>&1
