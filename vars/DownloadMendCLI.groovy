def call() { 
      echo 'Downloading Mend CLI'
      sh 'curl https://downloads.mend.io/cli/linux_amd64/mend -o /usr/local/bin/mend && sudo chmod +x /usr/local/bin/mend'
}
