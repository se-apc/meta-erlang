include erlang.inc

DEPENDS += "erlang-native openssl"

require erlang-manifest.inc

PR = "r0"

PACKAGECONFIG ??= ""

# PACKAGECONFIG[odbc] = "--with-odbc,-without-odbc,libodbc"

TARGET_CC_ARCH += "${LDFLAGS}"

EXTRA_OEMAKE = "BUILD_CC='${BUILD_CC}'"

# EXTRA_OECONF = "--without-wx"
# EXTRA_OECONF:append_arm = " --disable-smp-support --disable-hipe"
# EXTRA_OECONF:append_armeb = " --disable-smp-support --disable-hipe"
EXTRA_OECONF:append_mipsel = " --disable-smp-support --disable-hipe"
EXTRA_OECONF:append_sh3 = " --disable-smp-support --disable-hipe"
EXTRA_OECONF:append_sh4 = " --disable-smp-support --disable-hipe"

NATIVE_BIN = "${STAGING_LIBDIR_NATIVE}/erlang/bin"

CACHED_CONFIGUREVARS += "ac_cv_prog_javac_ver_1_2=no ac_cv_prog_javac_ver_1_5=no erl_xcomp_sysroot=${STAGING_DIR_TARGET}"

# do_configure() {
#     cd ${S}; ./otp_build autoconf; cd -
#     cd ${S}/erts; autoreconf; cd -
#     cd ${S}/lib/wx; autoreconf; cd -

#     touch ${S}/lib/wx/SKIP
#     touch ${S}/lib/odbc/SKIP

#     . ${CONFIG_SITE}

#     SHLIB_LD='${CC}' \
#     oe_runconf

#     sed -i -e 's|$(ERL_TOP)/bin/dialyzer|${NATIVE_BIN}/dialyzer --output_plt $@ -pa $(ERL_TOP)/lib/kernel/ebin -pa $(ERL_TOP)/lib/stdlib/ebin|' lib/dialyzer/src/Makefile
# }

do_configure() {
    # touch ${S}/lib/ic/java_src/SKIP
    rm -Rf ${S}/lib/wx
    rm -Rf ${S}/lib/odbc

    cd ${S}; ./otp_build autoconf
    TARGET=${HOST_SYS} \
    oe_runconf
}

do_compile() {
    TARGET=${TARGET_SYS} \
    PATH=${NATIVE_BIN}:$PATH \
    oe_runmake noboot
}

do_install() {
    TARGET=${TARGET_SYS} \
    PATH=${NATIVE_BIN}:$PATH \
    oe_runmake 'INSTALL_PREFIX=${D}' install
    for f in erl start
        do sed -i -e 's:ROOTDIR=.*:ROOTDIR=${libdir}/erlang:' \
            ${D}/${libdir}/erlang/erts-*/bin/$f ${D}/${libdir}/erlang/bin/$f
    done

    rm -f ${D}/${libdir}/erlang/Install

    # Actually wx is not suitable with erlang embedded
    rm -rf ${D}/${libdir}/erlang/lib/wx-*
    chown -R root:root ${D}${libdir}/erlang
}

