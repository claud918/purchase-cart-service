#!/bin/bash

echo "Building the application..."
mvn clean package -DskipTests
echo "Build completed."