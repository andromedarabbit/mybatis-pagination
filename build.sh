#!/usr/bin/env bash

GIT_BRANCH=`git rev-parse --abbrev-ref HEAD`
GIT_COMMIT_ID=`git rev-parse --short HEAD`

JET_PATH=`which jet`
if test ! -f "$JET_PATH"
then
  brew tap caskroom/cask
  brew cask install jet
fi

jet steps --ci-branch=$GIT_BRANCH --ci-commit-id=$GIT_COMMIT_ID --tag=$GIT_BRANCH
