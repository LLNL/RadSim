#!/bin/bash

TEXT_ARG="${1:-}"

PY_DIR="$(pwd)"
CPP_DIR="${PY_DIR}/../cpp"
BUILD_DIR="${CPP_DIR}/build"

cp "${PY_DIR}/PdfCDF.csv" "${PY_DIR}/init_vis.mac" "${PY_DIR}/rtk.mac" "${CPP_DIR}"

cd "${CPP_DIR}"
mkdir -p "${BUILD_DIR}"
cd "${BUILD_DIR}"

cmake ..
make

if [ -n "$TEXT_ARG" ]; then
    ./RadSimBetaConverter -m rtk.mac

    for file in RadSim_h1*.csv; do
        base_filename=$(basename "$file")
        
        if [ -n "$TEXT_ARG" ]; then
            cp "$file" "${PY_DIR}/${base_filename%.csv}_${TEXT_ARG}.csv"
        else
            cp "$file" "${PY_DIR}/${base_filename}"
        fi
    done

else
    ./RadSimBetaConverter
fi