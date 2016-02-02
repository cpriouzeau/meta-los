
DESCRIPTION = "Wayland extension for KMS."
SRC_URI = "git://review.tizen.org/platform/adaptation/renesas_rcar/wayland-kms.git;branch=tizen"

S = "${WORKDIR}/git"

SRCREV = "${AUTOREV}"

DEPENDS = " wayland "

LICENSE="GPLv2"

LIC_FILES_CHKSUM = "file://LICENSE;md5=100890a2f5911865cf064f9e8b821666"

inherit autotools pkgconfig
