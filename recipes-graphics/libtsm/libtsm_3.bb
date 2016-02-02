SUMMARY = "Terminal-emulator State Machine"
DESCRIPTION = "TSM is a state machine for DEC VT100-VT520 compatible terminal emulators. "
HOMEPAGE = "http://www.freedesktop.org/wiki/Software/libtsm/"

SRC_URI="http://freedesktop.org/software/kmscon/releases/libtsm-3.tar.xz"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=be41eca402c741d9a6384aea14c75ae3"
SRC_URI[md5sum] = "c1b297a69d11a72f207ec35ae5ce7d69"
SRC_URI[sha256sum] = "114115d84a2bc1802683871ea2d70a16ddeec8d2f8cde89ebd2046d775e6cf07"

inherit autotools pkgconfig
