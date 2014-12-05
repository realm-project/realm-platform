#!/bin/bash

java -jar antlr-4.3-complete.jar -visitor -package net.objectof.model.query.parser QueryParser.g4 QueryLexer.g4
