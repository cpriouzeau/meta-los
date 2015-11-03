# Copyright (C) 2015 Christophe Priouzeau <christophe.priouzeau@linaro.org>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "gstreamer player interface"
HOMEPAGE = "https://github.com/sdroege/gst-player"
LICENSE = "LGPLv2.1"
DEPENDS = "gstreamer1.0 gstreamer1.0-plugins-base"
PR = "r0"

LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

SRC_URI = "git://github.com/sdroege/gst-player;protocol=https"
SRCREV = "1c155babf01c5a930ffa9fb19b14238d3577d586"

S = "${WORKDIR}/git"

inherit gettext autotools pkgconfig

EXTRA_OECONF += " \
    --disable-gtk-doc \
"

do_configure_prepend() {
    echo "" >> ${S}/ChangeLog
}
