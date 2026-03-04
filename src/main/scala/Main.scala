import layoutz.*

import scala.scalanative.libc.*
import scala.scalanative.unsafe.*

// 플랫폼에 따라 다른 외부 함수 호출
// @extern
// object sys {
//   // Linux용
//   // def sysinfo(info: Ptr[Byte]): Int = extern

//   // Windows용 (kernel32.dll의 GetTickCount64 사용)
//   // 부팅 후 경과된 시간을 밀리초(ms) 단위로 반환합니다.
//   def GetTickCount64(): Long = extern
// }

@extern
object win {
  // 콘솔 출력 코드 페이지를 설정하는 함수
  def SetConsoleOutputCP(codePage: Int): Int = extern
}

/* Model your domain as usual */
case class TypeError(
    file: String,
    line: Int,
    prefix: String,
    bad: String,
    expected: String,
    found: String,
    hint: String
)

/* Bridge to layoutz with tiny pure functions using layoutz `Element`s */
def typeError(e: TypeError): Element = {
  val ln = e.line.toString
  val bar = "│".color(Color.Cyan)
  layout(
    rowTight(
      "── TYPE MISMATCH ".color(Color.Cyan),
      s"${e.file}:${e.line}".style(Style.Dim),
      " ────────".color(Color.Cyan)
    ),
    rowTight(ln.color(Color.Cyan), space, bar, space, e.prefix, e.bad),
    rowTight(
      space(ln.length + 1),
      bar,
      space,
      space(e.prefix.length),
      ("^" * e.bad.length + " ").color(Color.Red),
      "expected ",
      e.expected.color(Color.Green),
      ", found ",
      e.found.color(Color.Red)
    ),
    rowTight(
      space(ln.length + 1),
      bar,
      space,
      "hint: ".color(Color.Cyan),
      e.hint
    )
  )
}

/* Compose and nest at will */
val demo = layout(
  underline("─", Color.BrightCyan)("Layoutz - レイアウツ 🌍🌸").center(),
  row(
    statusCard("API", "LIVE").border(Border.Round).color(Color.Green),
    statusCard("DB", "99.9%")
      .border(Border.Double)
      .color(Color.BrightMagenta),
    statusCard("Système", "OK").border(Border.Thick).color(Color.Cyan)
  ).center(),
  "",
  box("Composition")(
    columns(
      plot(width = 30, height = 8)(
        Series(
          (0 to 60).map(i => (i.toDouble, math.sin(i * 0.15) * 3)),
          "sin"
        ).color(Color.Cyan),
        Series(
          (0 to 60).map(i => (i.toDouble, math.cos(i * 0.15) * 3)),
          "cos"
        ).color(Color.Magenta)
      ),
      tree("src")(
        tree("main")(
          tree("App.scala")
        ),
        tree("test")(
          tree("AppSpec.scala")
        )
      )
    )
  ).border(Border.Round).center(),
  "",
  typeError(
    TypeError(
      "Foo.scala",
      42,
      "val x: Int = ",
      "getName()",
      "Int",
      "String",
      "try `.toInt`"
    )
  )
)

@main def run(): Unit = {
  println("Hello Scala Native!")

  // val os = System.getProperty("os.name").toLowerCase

  // if (os.contains("win")) {
  //   // Windows 로직
    win.SetConsoleOutputCP(65001)
  //   val uptimeMs = sys.GetTickCount64()
  //   val totalSeconds = uptimeMs / 1000
  //   printUptime(totalSeconds)

  //   println("\n계속하려면 엔터 키를 누르세요...")
  //   scala.io.StdIn.readLine()
  // } else {
  //   // Linux 로직
  //   // val info = stackalloc[Byte](128)
  //   // if (sys.sysinfo(info) == 0) {
  //   // val totalSeconds = !(info.asInstanceOf[Ptr[Long]])
  //   // printUptime(totalSeconds)
  //   // }
  // }

  /* Get pretty strings with `render` */
  // println(demo.render)
  SimpleGame.run
}

// def printUptime(totalSeconds: Long): Unit = {
//   val days = totalSeconds / (24 * 3600)
//   val hours = (totalSeconds % (24 * 3600)) / 3600
//   val minutes = (totalSeconds % 3600) / 60
//   val seconds = totalSeconds % 60
//   println(s"가동 시간: $days 일 $hours 시 $minutes 분 $seconds 초")
// }
