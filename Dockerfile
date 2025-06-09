FROM ubuntu:latest
LABEL authors="Desktop"

ENTRYPOINT ["top", "-b"]