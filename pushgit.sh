#!/bin/bash

# Prompt the user for a commit message
read -p "Enter your commit message: " message

# Run git commands
git add .
git commit -m "$message"
git push origin main
git push origin master
