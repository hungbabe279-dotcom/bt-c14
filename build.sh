#!/bin/bash
set -e
echo "Building Java application..."
mvn clean package
echo "Build complete!"
