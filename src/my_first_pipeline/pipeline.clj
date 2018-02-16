(ns my-first-pipeline.pipeline
  (:use [lambdacd.steps.control-flow]
        [my-first-pipeline.steps])
  (:require
        [lambdacd.steps.manualtrigger :as manualtrigger]))

(def pipeline-def
  `(
    (either
     manualtrigger/wait-for-manual-trigger
     wait-for-git)

    (with-workspace
      clone
      server-package
      where
      copy-outside-of-workspace)
   ;(in-parallel
    ;  some-step-that-echos-foo
    ;  some-step-that-echos-bar)
    start-the-app))

    ;where))
