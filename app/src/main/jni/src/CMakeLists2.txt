cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_VERBOSE_MAKEFILE on)
set(LOCAL_PATH ${CMAKE_CURRENT_SOURCE_DIR})
set(distribution_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../../../../distribution)

#编译精简虚拟机
include_directories(
        .
        lib
)

add_library(lib_mr_vm_mini STATIC IMPORTED)
set_target_properties(lib_mr_vm_mini PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/mr_pre/lib/${ANDROID_ABI}/libmr_vm_mini.a)

add_library( # Sets the name of the library.
        mrpoid

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        emulator.c
        dsm.c
        network.c
        utils.c
        cport.c
        register_natives.c
        encode.c
        font/font_sky16_2.c
        lib/TimeUtils.c
        lib/JniUtils.c
        #	font/tsffont.c
        )
# 私有definitions
target_compile_definitions(mrpoid PRIVATE -DDSM_MINI)
target_include_directories(mrpoid PRIVATE
        ${distribution_DIR}/mr_pre/include)
target_link_libraries( # Specifies the target library.
        mrpoid
        # Links the target library to the log library
        # included in the NDK.
        log
        jnigraphics
        m
        c
        z
        lib_mr_vm_mini
        )


#编译完整虚拟机###############
include_directories(
        .
        lib
)

add_library(lib_mr_vm_full STATIC IMPORTED)
set_target_properties(lib_mr_vm_full PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/mr_pre/lib/${ANDROID_ABI}/libmr_vm_full.a)

add_library( # Sets the name of the library.
        mrpoid2

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        emulator.c
        dsm.c
        network.c
        utils.c
        vm.c
        lib/msgqueue.c
        register_natives.c
        encode.c
        lib/TimeUtils.c
        font/font_sky16_2.c
        lib/JniUtils.c
        )
# 私有definitions
target_compile_definitions(mrpoid2 PRIVATE -DDSM_FULL)
target_include_directories(mrpoid2 PRIVATE
        ${distribution_DIR}/mr_pre/include)
target_link_libraries( # Specifies the target library.
        mrpoid2
        # Links the target library to the log library
        # included in the NDK.
        m
        log
        c
        z
        jnigraphics
        lib_mr_vm_full
        )