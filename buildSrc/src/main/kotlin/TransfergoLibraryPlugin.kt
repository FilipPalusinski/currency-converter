import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class TransfergoLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target.pluginManager) {
            apply("com.android.library")
            apply("org.jetbrains.kotlin.android")
        }

        val androidExtension = target.extensions.getByName("android")
        if (androidExtension is LibraryExtension) {
            androidExtension.apply {
                compileSdk = SdkVersions.compileSdk
                defaultConfig {
                    targetSdkVersion(SdkVersions.targetSdk)
                    minSdk = SdkVersions.minSdk
                }

                compileOptions {
                    isCoreLibraryDesugaringEnabled = true
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }

                target.tasks.withType<KotlinCompile>().configureEach {
                    kotlinOptions {
                        jvmTarget = "11"
                    }
                }

            }
        }

        val libs = target.extensions.getByType<VersionCatalogsExtension>().named("libs")
        target.dependencies {
            add("coreLibraryDesugaring", libs.findLibrary("desugaring-core").get())
        }
    }
}
