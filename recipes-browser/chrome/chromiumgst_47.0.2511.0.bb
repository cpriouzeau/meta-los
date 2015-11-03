DESCRIPTION = "Chromium browser"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"
DEPENDS = "xz-native pciutils pulseaudio cairo nss zlib-native libav libgnome-keyring cups ninja-native gconf libexif pango libdrm libxslt libxslt-native"

SRC_URI = " \
        http://gsdview.appspot.com/chromium-browser-official/chromium-${PV}.tar.xz \
        file://include.gypi \
        file://oe-defaults.gypi \
        file://google-chrome \
        file://google-chrome.desktop \
        file://0001-Remove-hard-coded-values-for-CC-and-CXX.patch \
        file://0002-unistd-2.patch \
        file://0003-test-correction.patch \
        file://0004-Correct-perf-test-remoting-dependency.patch \
        "

S = "${WORKDIR}/chromium-${PV}"

SRC_URI[md5sum] = "1b53d2fc5e497f5581452489ae66c309"
SRC_URI[sha256sum] = "b3d01d5ad608fc5f78a3ae0200a2c60365f41be438fcc077e71e936ed1481dbb"

# for aarch64
SRC_URI += " file://0005-fix_64_bit_builds.patch "
SRC_URI += " file://0006-fix_mesa_GL_RED_support_detection.patch "

# ------------------------
# gstreamer
DEPENDS += " gstreamer1.0-player "

# -------------------------
#           wayland
DEPENDS += " wayland libxkbcommon "


OZONE_WAYLAND_GIT_DESTSUFFIX = "ozone-wayland-git"
OZONE_WAYLAND_GIT_BRANCH = "master"
OZONE_WAYLAND_GIT_SRCREV = "1d8b0cba33b3fdbf7fd6c8d3be83a558d87e9373"
SRC_URI += " git://github.com/01org/ozone-wayland.git;destsuffix=${OZONE_WAYLAND_GIT_DESTSUFFIX};branch=${OZONE_WAYLAND_GIT_BRANCH};rev=${OZONE_WAYLAND_GIT_SRCREV} "

do_unpack[postfuncs] += "copy_ozone_wayland_files"
do_patch[prefuncs]   += "add_ozone_wayland_patches"

copy_ozone_wayland_files() {
	# ozone-wayland sources must be placed in an "ozone"
	# subdirectory in ${S} in order for the .gyp build
	# scripts to work
	cp -r ${WORKDIR}/ozone-wayland-git ${S}/ozone
}

#?????? change path
python add_ozone_wayland_patches() {
    import glob
    srcdir = d.getVar('S', True)
    thisdir = d.getVar('THISDIR', True)
    prefix = ' file://' + srcdir
    # find all ozone-wayland patches and add them to SRC_URI
    d.appendVar('SRC_URI', prefix + '/ozone/patches/0001-Browser-Support-Desktop-Aura-creation-on-Ozone.patch')
    d.appendVar('SRC_URI', prefix + '/ozone/patches/0002-Remove-usage-of-DesktopFactory.patch')
    d.appendVar('SRC_URI', prefix + '/ozone/patches/0003-Add-support-to-provide-external-ozone-files-in-views.patch')
    d.appendVar('SRC_URI', prefix + '/ozone/patches/0004-Add-support-for-checking-DesktopWindow-with-Ozone-an.patch')
    d.appendVar('SRC_URI', prefix + '/ozone/patches/0005-Media-Build-VaapiPictureWayland-as-part-of-Media.patch')
    d.appendVar('SRC_URI', prefix + '/ozone/patches/0006-Fix-crash-when-switching-to-console-VT-mode.patch')
    d.appendVar('SRC_URI', prefix + '/ozone/patches/0007-Add-needed-support-in-PlatformWindow.patch')
    d.appendVar('SRC_URI', prefix + '/ozone/patches/0008-Add-file-picker-support-using-WebUI.patch')
    d.appendVar('SRC_URI', prefix + '/ozone/patches/0010-Remove-ATK-dependency.patch')
    d.appendVar('SRC_URI', prefix + '/ozone/patches/0011-Gardening-Adopt-to-https-codereview.chromium.org-129.patch')
    d.appendVar('SRC_URI', prefix + '/ozone/patches/0012-Use-InputMethodAuraLinux-for-Ozone-implementations-o.patch')
    d.appendVar('SRC_URI', prefix + '/ozone/patches/0013-Add-drag-and-drop-interfaces-to-PlatformWindowDelega.patch')

    d.appendVar('SRC_URI', ' file://' + thisdir + '/chromiumgst/0007-Samsung-Gstreamer-based-on-68cbb438624b86a3017914adf.patch')
    d.appendVar('SRC_URI', ' file://' + thisdir + '/chromiumgst/0008-Gstreamer-Werror-management.patch')

    d.appendVar('SRC_URI', ' file://' + thisdir + '/chromiumgst/aarch64/0003-AARCH64-correct-sandbox-restriction-with-architectur.patch')
    d.appendVar('SRC_URI', ' file://' + thisdir + '/chromiumgst/0001-Add-dependency-with-ozone-events-evdev.patch')

}

trace_log() {
    bbwarn "=======>SRC_URI=${SRC_URI}"
}

# include.gypi exists only for armv6 and armv7a and there isn't something like COMPATIBLE_ARCH afaik
COMPATIBLE_MACHINE = "(-)"
COMPATIBLE_MACHINE_i586 = "(.*)"
COMPATIBLE_MACHINE_x86-64 = "(.*)"
COMPATIBLE_MACHINE_armv6 = "(.*)"
COMPATIBLE_MACHINE_armv7a = "(.*)"
COMPATIBLE_MACHINE_aarch64 = "(.*)"

inherit gettext

PACKAGECONFIG ??= "use-egl"

CHROMIUM_BUILD_TYPE = "Release"

# this makes sure the dependencies for the EGL mode are present; otherwise, the configure scripts
# automatically and silently fall back to GLX
PACKAGECONFIG[use-egl] = ",,virtual/egl virtual/libgles2"


EXTRA_OEGYP = " \
    -Dangle_use_commit_id=0 \
    -Dclang=0 \
    -Dhost_clang=0 \
    -Ddisable_fatal_linker_warnings=1 \
    -I ${WORKDIR}/oe-defaults.gypi \
    -I ${WORKDIR}/include.gypi \
    -f ninja \
"
ARMFPABI_armv7a = "${@bb.utils.contains('TUNE_FEATURES', 'callconvention-hard', 'arm_float_abi=hard', 'arm_float_abi=softfp', d)}"

CHROMIUM_EXTRA_ARGS ?= " \
    ${@bb.utils.contains('PACKAGECONFIG', 'use-egl', '--use-gl=egl', '', d)} \
"

GYP_DEFINES = "${ARMFPABI} release_extra_cflags='-Wno-error=unused-local-typedefs' sysroot=''"
# wayland addons
GYP_DEFINES += " use_ash=1 use_aura=1 chromeos=0 use_ozone=1 use_xkbcommon=1 "


do_configure() {
    cd ${S}
    GYP_DEFINES="${GYP_DEFINES}" export GYP_DEFINES
    # replace LD with CXX, to workaround a possible gyp issue?
    LD="${CXX}" export LD
    CC="${CC}" export CC
    CXX="${CXX}" export CXX
    CC_host="${BUILD_CC}" export CC_host
    CXX_host="${BUILD_CXX}" export CXX_host
    build/gyp_chromium --depth=. ${EXTRA_OEGYP}
}

do_compile() {
    V8_MISSING_BINARIES="natives_blob"
    # build with ninja
    ninja -C ${S}/out/${CHROMIUM_BUILD_TYPE} -j${BB_NUMBER_THREADS} chrome chrome_sandbox  ${V8_MISSING_BINARIES}
}

do_install() {
    if [ -f "${WORKDIR}/google-chrome" ]; then
        install -Dm 0755 ${WORKDIR}/google-chrome ${D}${bindir}/google-chrome
        sed -i "s/^CHROME_EXTRA_ARGS=\"\"/CHROME_EXTRA_ARGS=\"${CHROMIUM_EXTRA_ARGS}\"/" ${D}${bindir}/google-chrome
    fi
    if [ -f "${B}/out/${CHROMIUM_BUILD_TYPE}/chrome_sandbox" ]; then
        install -Dm 4755 ${B}/out/${CHROMIUM_BUILD_TYPE}/chrome_sandbox ${D}${sbindir}/chrome-devel-sandbox
    fi
    if [ -f "${B}/out/${CHROMIUM_BUILD_TYPE}/chrome" ]; then
        install -Dm 0755 ${B}/out/${CHROMIUM_BUILD_TYPE}/chrome ${D}${bindir}/${BPN}/chrome
    fi
    if [ -f "${B}/out/${CHROMIUM_BUILD_TYPE}/icudtl.dat" ]; then
        install -Dm 0644 ${B}/out/${CHROMIUM_BUILD_TYPE}/icudtl.dat ${D}${bindir}/${BPN}/icudtl.dat
    fi
    if [ -f "${B}/out/${CHROMIUM_BUILD_TYPE}/icudtl.dat" ]; then
        install -Dm 0644 ${B}/out/${CHROMIUM_BUILD_TYPE}/icudtl.dat ${D}${bindir}/${BPN}/icudtl.dat
    fi
    if [ -f "${B}/out/${CHROMIUM_BUILD_TYPE}/natives_blob.bin" ]; then
        install -Dm 0644 ${B}/out/${CHROMIUM_BUILD_TYPE}/natives_blob.bin ${D}${bindir}/${BPN}/natives_blob.bin
    fi
    if [ -f "${WORKDIR}/google-chrome.desktop" ]; then
        install -Dm 0644 ${WORKDIR}/google-chrome.desktop ${D}${datadir}/applications/google-chrome.desktop
    fi
    #Chromium plugins libs
    for f in libpdf.so libosmesa.so libffmpegsumo.so; do
        if [ -f "${B}/out/${CHROMIUM_BUILD_TYPE}/$f" ]; then
            install -Dm 0644 ${B}/out/${CHROMIUM_BUILD_TYPE}/$f ${D}${libdir}/${BPN}/$f
        fi
    done

    #Chromium *.pak files and CEF pak files ( prefixed with cef_
    for f in content_resources.pak keyboard_resources.pak chrome_100_percent.pak product_logo_48.png resources.pak \
             cef_100_percent.pak cef_200_percent.pak cef_resources.pak cef.pak \
             locales/en-US.pak; do
        if [ -f "${B}/out/${CHROMIUM_BUILD_TYPE}/$f" ]; then
            install -Dm 0644 ${B}/out/${CHROMIUM_BUILD_TYPE}/$f ${D}${bindir}/${BPN}/$f
        fi
    done
}

FILES_${PN} = "${bindir}/${BPN} ${datadir}/applications ${sbindir}/ ${libdir}/${BPN}/"
FILES_${PN} += "${bindir} ${libdir}"
FILES_${PN} += "${bindir}/${BPN}/*.bin"
FILES_${PN} += "${bindir}/${BPN}/*.pak"
FILES_${PN} += "${bindir}/${BPN}/locales/*.pak"
FILES_${PN}-dbg += "${bindir}/${BPN}/.debug/ ${libdir}/${BPN}/.debug/ ${libdir}/.debug/"

PACKAGE_DEBUG_SPLIT_STYLE = "debug-without-src"
INSANE_SKIP_${PN} = "ldflags"
SOLIBS = ".so"
FILES_SOLIBSDEV = ""
