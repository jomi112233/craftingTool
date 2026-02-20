@echo off
echo Running PIT Mutation Testing...
mvn org.pitest:pitest-maven:mutationCoverage

pause
