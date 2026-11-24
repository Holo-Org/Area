# Build reproductible de l'APK Kotlin Multiplatform. Usage :
#   docker build --target export --output type=local,dest=out --build-arg AREA_SERVER_URL=http://10.0.2.2:8080 .
FROM gradle:9.6.1-jdk21-noble AS sdk
ARG CMDLINE_TOOLS=16111833
ENV ANDROID_HOME=/opt/android-sdk \
    PATH=/opt/android-sdk/cmdline-tools/latest/bin:$PATH
RUN apt-get update && apt-get install -y --no-install-recommends curl unzip ca-certificates \
 && rm -rf /var/lib/apt/lists/*
# Plateforme et build-tools manquants sont ensuite téléchargés par AGP au premier build.
RUN mkdir -p $ANDROID_HOME/cmdline-tools \
 && curl -sSLo /tmp/clt.zip https://dl.google.com/android/repository/commandlinetools-linux-${CMDLINE_TOOLS}_latest.zip \
 && unzip -q /tmp/clt.zip -d /tmp && mv /tmp/cmdline-tools $ANDROID_HOME/cmdline-tools/latest && rm /tmp/clt.zip \
 && android --sdk=$ANDROID_HOME sdk install platform-tools

FROM sdk AS build
WORKDIR /app
COPY . .
ARG AREA_SERVER_URL
ENV AREA_SERVER_URL=${AREA_SERVER_URL}
RUN gradle :androidApp:assembleRelease --no-daemon --console=plain --max-workers=6

FROM scratch AS export
COPY --from=build /app/androidApp/build/outputs/apk/release/androidApp-release.apk /area-spike-kmp.apk
