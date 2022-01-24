def ci(){

    pipeline {
        agent any
        stages {
            stage("-1 logs"){
                sh "echo 'branchname: '" + BRANCH_NAME
                sh 'printenv'
            }
            stage("1 Compile"){
                //- Compilar el código con comando maven
                sh "echo 'Compile Code!'"
                // Run Maven on a Unix agent.
                //sh "mvn clean compile -e"
            }
            stage("2 Unit Test"){
            //- Testear el código con comando maven
                sh "echo 'Test Code!'"
                // Run Maven on a Unix agent.
                //sh "mvn clean test -e"

            }
            stage("3 Build jar"){
            //- Generar artefacto del código compilado.
                sh "echo 'Build .Jar!'"
                // Run Maven on a Unix agent.
                sh "mvn clean package -e"
            }
            stage("4 SonarQube"){
            //- Generar análisis con sonar para cada ejecución
            //- Cada ejecución debe tener el siguiente formato de nombre: QUE ES EL NOMBRE DE EJECUCIÓN ??
                //- {nombreRepo}-{rama}-{numeroEjecucion} ejemplo:
                //- ms-iclab-feature-estadomundial(Si está usando el CRUD ms-iclab-feature-[nombre de su crud])
                withSonarQubeEnv('SonarQubeServer') {
                    sh "echo 'SonarQube'"
                    sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=githubfull'

                }
            }
            stage("5 Nexus"){
                //- Subir el artefacto creado al repositorio privado de Nexus.
                //- Ejecutar este paso solo si los pasos anteriores se ejecutan de manera correcta.
                nexusPublisher nexusInstanceId: 'nexus', 
                nexusRepositoryId: 'devops-usach-nexus', 
                packages: [[$class: 'MavenPackage', 
                    mavenAssetList: [[classifier: '', 
                                    extension: '',
                                    filePath: 'build/DevOpsUsach2020-0.0.1.jar']],
                    mavenCoordinate: [artifactId: 'DevOpsUsach2020', 
                                    groupId: 'com.devopsusach2020', 
                                    packaging: 'jar', 
                                    version: VERSION]]]
            }
            stage("6 gitCreateRelease"){
            //- Crear rama release cuando todos los stages anteriores estén correctamente ejecutados.
            //- Este stage sólo debe estar disponible para la rama develop.
                sh "echo 'gitCreateRelease'"
            }
        }
    }
}

def citest2(){
    stage("-1 logs"){
        steps {
            sh "java -version"
        }
      
    }
    stage("-2 logs"){
        steps {
            sh "printenv"
        }
    }
}