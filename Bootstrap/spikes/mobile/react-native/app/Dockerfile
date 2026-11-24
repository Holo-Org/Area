# Build reproductible de l'APK React Native / Expo. Usage :
#   docker build --target export --output type=local,dest=out --build-arg EXPO_PUBLIC_AREA_SERVER_URL=http://10.0.2.2:8080 .
FROM eclipse-temurin:21-jdk-noble AS sdk
ARG NODE_VERSION=26.13.1
ARG CMDLINE_TOOLS=16111833
ENV ANDROID_HOME=/opt/android-sdk \
    PATH=/opt/node/bin:/opt/android-sdk/cmdline-tools/latest/bin:$PATH
RUN apt-get update && apt-get install -y --no-install-recommends curl unzip xz-utils git ca-certificates libatomic1 \
 && rm -rf /var/lib/apt/lists/*
RUN mkdir -p /opt/node && curl -sSL https://nodejs.org/dist/v${NODE_VERSION}/node-v${NODE_VERSION}-linux-x64.tar.xz | tar xJ -C /opt/node --strip-components=1
RUN mkdir -p $ANDROID_HOME/cmdline-tools \
 && curl -sSLo /tmp/clt.zip https://dl.google.com/android/repository/commandlinetools-linux-${CMDLINE_TOOLS}_latest.zip \
 && unzip -q /tmp/clt.zip -d /tmp && mv /tmp/cmdline-tools $ANDROID_HOME/cmdline-tools/latest && rm /tmp/clt.zip \
 && android --sdk=$ANDROID_HOME sdk install platform-tools platforms/android-36 build-tools/36.0.0

FROM sdk AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
ARG EXPO_PUBLIC_AREA_SERVER_URL
# Limité à 2 ABI et 4 workers : 4 ABI x 12 cœurs a provoqué un OOM sur une machine 15 Go.
ARG ANDROID_ABIS=arm64-v8a,x86_64
ENV EXPO_PUBLIC_AREA_SERVER_URL=${EXPO_PUBLIC_AREA_SERVER_URL}
RUN npx expo prebuild --platform android --no-install \
 && cd android && ./gradlew assembleRelease --no-daemon -q --max-workers=4 -PreactNativeArchitectures=${ANDROID_ABIS}

FROM scratch AS export
COPY --from=build /app/android/app/build/outputs/apk/release/app-release.apk /area-spike-react-native.apk
