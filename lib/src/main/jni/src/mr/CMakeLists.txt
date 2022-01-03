cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_VERBOSE_MAKEFILE on)

set(LOCAL_PATH ${CMAKE_CURRENT_SOURCE_DIR})
set(MINI_VM_VERSION 2011)
set(VM_VERSION 1968)
set(MINI_TARGETDIR  )

add_definitions(-c -littleend -O2 -DTARGET_MOD -DMTK_MOD -DMR_VERSION=${MINI_VM_VERSION} -DMR_PLAT_DRAWTEXT -DMR_PLAT_U2C)

include_directories(include .)

add_library( # Sets the name of the library.
        mr_vm_mini

        # Sets the library as a shared library.
        STATIC

        # Provides a relative path to your source file(s).
        mythroad_mini.c
        mr_gb2312.c
        mr_unzip.c
        mr_util.c
        mr_encode.c
        mr_inflate.c
        mr_graphics.c
        )
# copy out the lib binary... need to leave the static lib around to pass gradle check
set(distribution_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../../../../../distribution)
set_target_properties(mr_vm_mini
        PROPERTIES
        ARCHIVE_OUTPUT_DIRECTORY
        "${distribution_DIR}/mr_pre/lib/${ANDROID_ABI}")

# copy out lib header file...
add_custom_command(TARGET mr_vm_mini POST_BUILD
        COMMAND "${CMAKE_COMMAND}" -E
        copy "${CMAKE_CURRENT_SOURCE_DIR}/mrporting.h"
        "${distribution_DIR}/mr_pre/include/mrporting.h"
        #                   **** the following 2 lines are for potential future debug purpose ****
        #                   COMMAND "${CMAKE_COMMAND}" -E
        #                   remove_directory "${CMAKE_CURRENT_BINARY_DIR}"
        COMMENT "Copying gmath to output directory")


#完全版

add_definitions(-c -littleend -O2 -DTARGET_MOD -DMTK_MOD -DMR_VERSION=${VM_VERSION} -DMR_PLAT_DRAWTEXT -DMR_PLAT_U2C)

include_directories(include . src tomr/include)

add_library( # Sets the name of the library.
        mr_vm_full

        # Sets the library as a shared library.
        STATIC

        # Provides a relative path to your source file(s).
        src/mr_api.c
        src/mr_debug.c
        src/mr_do.c
        src/mr_dump.c
        src/mr_func.c
        src/mr_gc.c
        src/mr_mem.c
        src/mr_opcodes.c
        src/mr_object.c
        src/mr_state.c
        src/mr_string.c
        src/mr_table.c
        src/mr_tm.c
        src/mr_undump.c
        src/mr_vm.c
        src/mr_zio.c
        src/mr_noparser.c
        #                 src/lparser.c
        #                 src/mr_cect.c
        #                 src/llex.c
        #                 src/lcode.c


        src/lib/mr_auxlib.c
        src/lib/mr_auxiliar.c
        src/lib/mr_baselib.c
        src/lib/mr_tablib.c
        src/lib/mr_strlib.c
        src/lib/mr_socket_target.c
        src/lib/mr_tcp_target.c
        src/lib/mr_iolib_target.c
        #                  src/loadlib.c
        #                  src/ldblib.c


        mythroad.c
        mr_gb2312.c
        mr_pluto.c
        mr_unzip.c
        mr_util.c
        mr_encode.c
        mr_graphics.c
        mr_inflate.c
        #                    src/mr_maketo.c
        #                    src/MySprite.c
        #                    src/pluto.c
        #                    src/Mylib.c


        tomr/src/tomr_event.c
        tomr/src/tomr_is.c
        tomr/src/tomr_map.c
        tomr/src/tomr_push.c
        tomr/src/tomr_to.c
        )
# copy out the lib binary... need to leave the static lib around to pass gradle check
set(distribution_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../../../../../distribution)
set_target_properties(mr_vm_full
        PROPERTIES
        ARCHIVE_OUTPUT_DIRECTORY
        "${distribution_DIR}/mr_pre/lib/${ANDROID_ABI}")

# copy out lib header file...
add_custom_command(TARGET mr_vm_full POST_BUILD
        COMMAND "${CMAKE_COMMAND}" -E
        copy "${CMAKE_CURRENT_SOURCE_DIR}/mrporting.h"
        "${distribution_DIR}/mr_pre/include/mrporting.h"
        #                   **** the following 2 lines are for potential future debug purpose ****
        #                   COMMAND "${CMAKE_COMMAND}" -E
        #                   remove_directory "${CMAKE_CURRENT_BINARY_DIR}"
        COMMENT "Copying gmath to output directory")