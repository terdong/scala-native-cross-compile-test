scalaVersion := "3.3.7" // A Long Term Support version.

name := "hello-native"

enablePlugins(ScalaNativePlugin)

// set to Debug for compilation details (Info is default)
logLevel := Level.Info

// import to add Scala Native options
import scala.scalanative.build._

// defaults set with common options shown
nativeConfig ~= { c =>
  c.withLTO(LTO.thin)
    .withMode(Mode.releaseFast)
    .withGC(GC.immix)
}

// lazy val deploy = taskKey[Unit]("배포용 바이너리를 루트의 dist 폴더로 집결시킵니다.")

// deploy := {
//   // 1. 배포 모드로 빌드 실행 (위에서 설정한 releaseFast 적용)
//   val binary = (Compile / nativeLinkReleaseFast).value
//   val rootDist = baseDirectory.value / "dist"

//   // 2. dist 폴더 초기화 및 생성
//   if (rootDist.exists()) IO.delete(rootDist)
//   IO.createDirectory(rootDist)

//   val config = (Compile / nativeConfig).value
//   val fileName = config.targetName.getOrElse(name.value)
//   val dest = rootDist / fileName

//   // 3. 파일 복사 및 권한 부여
//   IO.copyFile(binary, dest)
//   dest.setExecutable(true)

//   // 4. (추가) 연관된 설정파일 등이 있다면 여기서 함께 복사
//   // val confFile = baseDirectory.value / "config.conf"
//   // if (confFile.exists()) IO.copyFile(confFile, rootDist / "config.conf")

//   println("=" * 40)
//   println(s"🚀 배포 준비 완료!")
//   println(s"📍 위치: ${rootDist.absolutePath}")
//   println("=" * 40)
// }
