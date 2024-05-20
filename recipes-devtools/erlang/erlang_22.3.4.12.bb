include erlang.inc
include erlang-${PV}.inc
require erlang-${PV}-manifest.inc

PR = "r0"

FILESEXTRAPATHS:prepend := "${THISDIR}/files/22.3:"
