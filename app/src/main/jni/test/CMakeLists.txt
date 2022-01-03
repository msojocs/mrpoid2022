# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_VERBOSE_MAKEFILE on)


add_library( # Sets the name of the library.
        jtest

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        test.c
        )
target_link_libraries( # Specifies the target library.
        jtest
        # Links the target library to the log library
        # included in the NDK.
        log
        )