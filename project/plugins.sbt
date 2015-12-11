logLevel := Level.Warn

resolvers ++= Seq("Typesafe Repository" at "https://repo.typesafe.com/typesafe/releases/",
  "jgit-repo" at "http://download.eclipse.org/jgit/maven",
  "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")
