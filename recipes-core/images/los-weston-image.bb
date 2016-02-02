DESCRIPTION = "Linaro OpenSDK image."
SUMMARY = "Basic Wayland image with Weston"

PV = "1.0.0"
PR = "r1"

LICENSE = "MIT"

inherit core-image distro_features_check extrausers

REQUIRED_DISTRO_FEATURES = "wayland pam systemd"

#
# Display part addons
#
IMAGE_DISPLAY_PART = " \
    libdrm \
    libdrm-tests \
    weston \
    weston-init \
    weston-cfg-systemd \
    kmscon \
"

IMAGE_EXAMPLES_PART = "\
    weston-examples \
    clutter-1.0-examples \
    "
#
# Network part
#
IMAGE_NETWORK_PART = "\
    wireless-tools \
    libnl \
    wpa-supplicant \
    connman \
"

#
# INSTALL addons
#
IMAGE_INSTALL += " \
    kernel-modules \
    \
    ${IMAGE_DISPLAY_PART} \
    ${IMAGE_NETWORK_PART} \
    ${IMAGE_EXAMPLES_PART} \
    \
    packagegroup-core-boot \
    ${ROOTFS_PKGMANAGE_BOOTSTRAP} \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    grep \
    util-linux \
    procps \
    kbd \
    dhcp-client \
    usbutils \
    pciutils \
    file \
    bc \
    coreutils\
    e2fsprogs \
    e2fsprogs-resize2fs \
    e2fsprogs-mke2fs \
    sysstat \
"

IMAGE_FEATURES += "splash package-management ssh-server-dropbear hwcodecs"

EXTRA_USERS_PARAMS = "\
useradd -p '' linaro; \
usermod -a -G weston-launch linaro; \
"
