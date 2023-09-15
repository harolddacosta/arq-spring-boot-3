pipeline {
  agent {
    kubernetes {
      yaml '''
        apiVersion: v1
        kind: Pod
        spec:
          hostAliases:
          - ip: "192.168.0.40"
            hostnames:
            - "assentsoftware.tplinkdns.com"
          imagePullSecrets:
          - name: regcred
          containers:
          - name: maven
            image: registry.assentsoftware.com/java-for-compile:3.9.0-jdk-17
            command:
            - cat
            tty: true
            volumeMounts:
              - name: maven-cache
                mountPath: /root/.m2/repository
            imagePullPolicy: Always
            env:
              - name: "TESTCONTAINERS_HOST_OVERRIDE"
                valueFrom:
                  fieldRef:
                    fieldPath: status.hostIP
              - name: "TESTCONTAINERS_RYUK_DISABLED"
                value: "true"
            envFrom:
            - secretRef:
                name: sonar-creds
          - name: dind-daemon
            image: docker:20.10.17-dind
            command: [dockerd-entrypoint.sh]
            securityContext: 
              privileged: true
            env: 
              - name: DOCKER_TLS_CERTDIR
                value: ""      
          volumes:
            - name: maven-cache
              persistentVolumeClaim:
                claimName: maven-cache-claim
        '''
    }
  }
  environment {
     BRANCH_NAME = "${env.GIT_BRANCH.split('/').size() == 1 ? env.GIT_BRANCH.split('/')[-1] : env.GIT_BRANCH.split('/')[1..-1].join('/')}"
  }
  stages {
    stage ('Initialize') {
      steps {
        container('maven') {
          sh '''
            echo "JAVA_VERSION = ${JAVA_VERSION}"
            echo "CI = ${CI}"
            echo "Branch name = ${BRANCH_NAME}"
          '''
        }
      }
    }
    stage('Compile') {
      steps {
        container('maven') {
          sh 'mvn -B clean compile'
        }
      }
    }
    stage('Code formatting check') {
      steps {
        container('maven') {
			sh 'mvn spotless:check' 
		}
      }
	}
    stage('Test and static code analysis') {
      steps {
        container('maven') {
          withSonarQubeEnv('Sonarqube in Kubernetes') {
            sh 'mvn -B verify sonar:sonar -Dsonar.projectKey=arq-demo -Dsonar.projectName="arq-demo" -Dembedded.postgresql.host="localhost"  -Dsonar.token="sqp_758b1b6f1a1c6c749c60dccce77bc3ef24d2c0de"'
          }
        }
      }
    }
    stage("Quality gate check") {
      steps {
        container('maven') {
          timeout(time: 5, unit: 'MINUTES') {
            waitForQualityGate abortPipeline: true
          }
        }
      }
    }
    stage('Deploying libraries') {
      steps {
        container('maven') {
          sh 'mvn -B deploy -Dmaven.test.skip=true'
        }
      }
    }
  }
  post {
    always {
      script {
        if (getContext(hudson.FilePath)) {
          junit allowEmptyResults: true, skipPublishingChecks: true, testResults: "**/target/**/*.xml"
        }
      }
    }
  }
}