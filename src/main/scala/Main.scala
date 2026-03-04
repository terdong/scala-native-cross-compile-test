// import scala.scalanative.libc.*
// import scala.scalanative.unsafe.*

// // C 언어의 sysinfo 구조체와 함수를 바인딩
// @extern
// object sys {
//   def sysinfo(info: Ptr[Byte]): Int = extern
// }

// @main def run(): Unit = {
//   println("Hello Scala Native!")
//   // 넉넉한 버퍼 할당
//   val info = stackalloc[Byte](128)

//   if (sys.sysinfo(info) == 0) {
//     // 0번 오프셋에서 uptime(Long) 값을 읽어옴
//     val totalSeconds = !(info.asInstanceOf[Ptr[Long]])

//     // 시간 파싱 로직
//     val days = totalSeconds / (24 * 3600)
//     val hours = (totalSeconds % (24 * 3600)) / 3600
//     val minutes = (totalSeconds % 3600) / 60
//     val seconds = totalSeconds % 60

//     println(s"가동 시간: $days 일 $hours 시 $minutes 분 $seconds 초")
//     println(s"(총 $totalSeconds 초)")
//   } else {
//     println("시스템 정보를 가져오는 데 실패했습니다.")
//   }
// }



import scala.scalanative.unsafe.*
import scala.scalanative.libc.*

// 플랫폼에 따라 다른 외부 함수 호출
@extern
object sys {
  // Linux용
  def sysinfo(info: Ptr[Byte]): Int = extern

  // Windows용 (kernel32.dll의 GetTickCount64 사용)
  // 부팅 후 경과된 시간을 밀리초(ms) 단위로 반환합니다.
  // def GetTickCount64(): Long = extern
}

@main def run(): Unit = {
  println("Hello Scala Native!")

  val os = System.getProperty("os.name").toLowerCase

  if (os.contains("win")) {
    // Windows 로직
    // val uptimeMs = sys.GetTickCount64()
    // val totalSeconds = uptimeMs / 1000
    // printUptime(totalSeconds)
  } else {
    // Linux 로직
    val info = stackalloc[Byte](128)
    if (sys.sysinfo(info) == 0) {
      val totalSeconds = !(info.asInstanceOf[Ptr[Long]])
      printUptime(totalSeconds)
    }
  }
}

def printUptime(totalSeconds: Long): Unit = {
  val days = totalSeconds / (24 * 3600)
  val hours = (totalSeconds % (24 * 3600)) / 3600
  val minutes = (totalSeconds % 3600) / 60
  val seconds = totalSeconds % 60
  println(s"가동 시간: $days 일 $hours 시 $minutes 분 $seconds 초")
}