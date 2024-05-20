include erlang.inc

inherit nativesdk

require erlang-manifest.inc

# Only do a single package for NativeSDK that includes everything
# PROVIDES+="${PN} "
# PACKAGES="${PN} "

ALLOW_EMPTY_${PN}="1"
DESCRIPTION_${PN}=""
RDEPENDS_${PN} = "nativesdk-ncurses "
FILES_${PN}+="${bindir} ${libdir}/erlang"

DEPENDS = "erlang-native openssl ncurses"

PR = "r0"

EXTRA_OECONF = "--with-ssl=${STAGING_DIR_NATIVE}"

CACHED_CONFIGUREVARS += "ac_cv_prog_javac_ver_1_2=no ac_cv_prog_javac_ver_1_5=no erl_xcomp_sysroot=${STAGING_DIR_NATIVE}"

do_configure() {
    rm -Rf ${S}/lib/wx
    rm -Rf ${S}/lib/odbc

    cd ${S}; ./otp_build autoconf
    TARGET=${HOST_SYS} \
    oe_runconf
}

do_compile:prepend() {
    export TARGET=${HOST_SYS}
}

do_install:prepend() {
    export TARGET=${HOST_SYS}
}

do_install:append() {
    chown -R root:root ${D}${libdir}/erlang
}
