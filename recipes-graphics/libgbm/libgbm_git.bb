DESCRIPTION = "GBM shim layer for wayland."

SRC_URI = "git://review.tizen.org/platform/upstream/libgbm;branch=tizen"

S = "${WORKDIR}/git"

SRCREV = "${AUTOREV}"

LICENSE="GPLv2"

LIC_FILES_CHKSUM = "file://debian/copyright;md5=721d2bad4cb4d5dcd6ea112782592134"

DEPENDS += " wayland-kms "

inherit autotools pkgconfig
