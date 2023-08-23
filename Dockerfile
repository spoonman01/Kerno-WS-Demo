ARG BASE_IMAGE_REPO_URI="docker.io"
FROM $BASE_IMAGE_REPO_URI/azul/zulu-openjdk-alpine:17.0.3-jre as BUILD
WORKDIR app

# Copy intentionally includes build & .gradle_home directories for caching
COPY . .
ENTRYPOINT ["/bin/sh", "-c", "./gradlew --no-daemon bootRun"]
