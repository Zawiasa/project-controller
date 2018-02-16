(ns my-first-pipeline.steps
  (:require [lambdacd.steps.shell :as shell]
            [lambdacd-git.core :as lambdacd-git]))



(lambdacd-git/init-ssh!)

(defn wait-for-git [args ctx]
  (lambdacd-git/wait-for-git ctx "git@github.com:paul931224/reagent-hello.git"
                             ; how long to wait when polling. optional, defaults to 10000
                             :ms-between-polls 1000
                             ; which refs to react to. optional, defaults to refs/heads/master
                             :ref "refs/heads/master"))


(defn clone [args ctx]
  (lambdacd-git/clone ctx "git@github.com:paul931224/reagent-hello.git" "master" (:cwd args)))


(defn custom-version-control [args ctx]
  (shell/bash ctx "/" "echo notsofasttraveller"))


(defn some-step-that-echos-foo [args ctx]
  (shell/bash ctx "/" "echo miafasz"))

(defn some-step-that-echos-bar [args ctx]
  (shell/bash ctx "/" "echo bar"))

(defn some-failing-step [args ctx]
  (shell/bash ctx "/" "echo \"i am going to fail now...\"" "exit 1"))

(defn server-package [{cwd :cwd} ctx]
    (println "server package cwd: " cwd)
    (shell/bash ctx cwd
      "lein uberjar"))

(defn where [{cwd :cwd} ctx]
    (println "server package cwd: " cwd)
    (shell/bash ctx cwd
      "ls target"))

(defn start-the-app [{cwd :cwd} ctx]
    (println "server package cwd: " cwd)
    (shell/bash ctx cwd
      "nohup java -jar hali.jar &"))


(defn copy-outside-of-workspace [{cwd :cwd} ctx]
    (println "server package cwd: " cwd)
    (shell/bash ctx cwd
      "cp target/reagent-hello.jar ../../hali.jar"))


    ;(shell/bash ctx cwd
    ;  "nohup java -jar my-first-pipeline-0.1.0-SNAPSHOT-standalone.jar &"))





(defn run-some-tests [args ctx]
  (shell/bash ctx (:cwd args) "./go test-clj"))
