(ns my-first-pipeline.core
  (:import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider)
  (:require
    [my-first-pipeline.pipeline :as pipeline]
    [my-first-pipeline.ui-selection :as ui-selection]
    [org.httpkit.server :as http-kit]
    [lambdacd.runners :as runners]

    [lambdacd.util :as util]
    [lambdacd.core :as lambdacd]
    [clojure.tools.logging :as log])
  (:gen-class))

(defn -main [& args]
  (let [;; the home dir is where LambdaCD saves all data.
        ;; point this to a particular directory to keep builds around after restarting
        home-dir "./hello"
        config   {:home-dir home-dir
                  :name     "The future of my apps"
                 ; the credentials-provider to use for HTTPS clones (e.g. UsernamePasswordCredentialsProvider))}
                  :ssh {:use-agent                true                         ; whether to use an SSH agent
                        :known-hosts-files        ["~/.ssh/known_hosts"
                                                   "/etc/ssh/ssh_known_hosts"] ; which known-hosts files to use for SSH connections
                        :identity-file            nil                          ; override the normal SSH behavior and explicitly specify a key to use
                        :strict-host-key-checking nil}}
           ;; initialize and wire everything together
        pipeline (lambdacd/assemble-pipeline pipeline/pipeline-def config)
        ;; create a Ring handler for the UI
        app      (ui-selection/ui-routes pipeline)]

    (log/info "LambdaCD Home Directory is " home-dir)
    ;; this starts the pipeline and runs one build after the other.
    ;; there are other runners and you can define your own as well.
    (runners/start-one-run-after-another pipeline)
    ;; start the webserver to serve the UI
    (http-kit/run-server app {:open-browser? false
                              :port          8080})))
