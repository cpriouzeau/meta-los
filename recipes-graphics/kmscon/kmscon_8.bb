SUMMARY = "System console for Linux"

DESCRIPTION = "kmscon is a system console for linux."

HOMEPAGE = "http://www.freedesktop.org/wiki/Software/kmscon/"

BUGTRACKER = "https://bugs.freedesktop.org/enter_bug.cgi?product=kmscon"

SRC_URI = "http://freedesktop.org/software/kmscon/releases/kmscon-${PV}.tar.xz"

DEPENDS = "libtsm xmlto-native docbook-xml-dtd4-native \
            docbook-xsl-stylesheets-native libxslt-native"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=7b09c62eae0d1148fb579f810f85e897"

SRC_URI[md5sum] = "90d39c4ef53a11c53f27be4a7e9acee4"
SRC_URI[sha256sum] = "0ab01c1cdc4fbd692ce765e80478bc2d9663a7c55a5c75cc7ac421366ee6ae2b"

inherit autotools pkgconfig
