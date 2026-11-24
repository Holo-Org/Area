# Build reproductible de l'APK Flutter. Usage :
#   docker build --target export --output type=local,dest=out --build-arg AREA_SERVER_URL=http://10.0.2.2:8080 .
FROM eclipse-temurin:21-jdk-noble AS sdk
ARG FLUTTER_VERSION=3.50.1
ARG CMDLINE_TOOLS=16111833
ENV ANDROID_HOME=/opt/android-sdk \
    PATH=/opt/flutter/bin:/opt/android-sdk/cmdline-tools/latest/bin:$PATH
RUN apt-get update && apt-get install -y --no-install-recommends curl unzip xz-utils git ca-certificates \
 && rm -rf /var/lib/apt/lists/*
RUN mkdir -p $ANDROID_HOME/cmdline-tools \
 && curl -sSLo /tmp/clt.zip https://dl.google.com/android/repository/commandlinetools-linux-${CMDLINE_TOOLS}_latest.zip \
 && unzip -q /tmp/clt.zip -d /tmp && mv /tmp/cmdline-tools $ANDROID_HOME/cmdline-tools/latest && rm /tmp/clt.zip \
 && android --sdk=$ANDROID_HOME sdk install platform-tools platforms/android-36 build-tools/36.0.0
RUN curl -sSL https://storage.googleapis.com/flutter_infra_release/releases/stable/linux/flutter_linux_${FLUTTER_VERSION}-stable.tar.xz | tar xJ -C /opt \
 && git config --global --add safe.directory /opt/flutter \
 && flutter --disable-analytics && flutter precache --android

FROM sdk AS build
WORKDIR /app
COPY pubspec.yaml pubspec.lock ./
RUN flutter pub get
COPY . .
ARG AREA_SERVER_URL
RUN flutter build apk --release --dart-define=AREA_SERVER_URL=${AREA_SERVER_URL}

FROM scratch AS export
COPY --from=build /app/build/app/outputs/flutter-apk/app-release.apk /area-spike-flutter.apk
