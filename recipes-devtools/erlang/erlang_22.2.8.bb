include erlang.inc
include erlang-${PV}.inc
require erlang-${PV}-manifest.inc

PR = "r1"

FILESEXTRAPATHS:prepend := "${THISDIR}/files/22.2:"
