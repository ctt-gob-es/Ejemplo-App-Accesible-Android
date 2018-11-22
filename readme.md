[guia]: https://administracionelectronica.gob.es/pae_Home/dam/jcr:3746627f-da12-40af-a5f5-20c42bb8c453/2017_Guia_accesibilidad_aplicaciones_moviles_apps.pdf
[en301549]: http://www.etsi.org/deliver/etsi_en/301500_301599/301549/01.01.02_60/en_301549v010102p.pdf
[D2016_2102]: https://www.boe.es/doue/2016/327/L00001-00015.pdf
[appacces]: http://tifyc-pmi.cc.uah.es/appacces
[ejemplo_ios]: https://github.com/ctt-gob-es/Ejemplo-App-Accesible-iOS
[atica2018]: http://www.cc.uah.es/Atica/Atica2018/

# ToDo Manager

 Esta aplicación para Android es un ejemplo del uso de la [GUÍA DE ACCESIBILIDAD DE APLICACIONES MÓVILES (APPS)][guia]
 para ayudar en el cumplimiento de la [directiva 2016/2102 de la Unión Europea][D2016_2102].
 Esta directiva obliga a todas las entidades del sector público a cumplir los requisitos de accesibilidad para la web y las aplicaciones móviles recogidos en la [EN 501 349][EN301549].
 En concreto, todas las aplicaciones móviles implicadas deberán satisfacerlos antes del 23 de junio de 2021 sin prórroga posible.

 Tanto la guía como los ejemplos de aplicación han sido desarrollados por un grupo de investigación de la [Universidad de Alcalá (UAH)][appacces]
 en colaboración con CEAPAT-IMSERSO, ILUNION y Observatorio de Accesibilidad del Ministerio de Hacienda y Función Pública del Gobierno de España. Este ejemplo también está disponible para la [plataforma iOS][ejemplo_ios].

## Estructura del ejemplo

 Este ejemplo cuenta con dos ramas. La rama master ha sido desarrollada sin tener en cuenta la accesibilidad desde el principio.
 En cambio, la rama accesible se ha creado para corregir las deficiencias que presentaba la aplicación en esta primera versión.

La aplicación no tiene una funcionalidad real y se centra sobre todo en la demostración de las diferentes características de accesibilidad que una aplicación de este tipo podría necesitar. Puede encontrar que la activación de algunos controles no desencadena una acción real.
 
 Se ha publicado un trabajo describiendo los errores de accesibilidad de la versión original de la aplicación y cómo se han corregido en la versión accesible. 
 El trabajo está incluido en el libro de actas del congreso ATICA 2018, con esta referencia:
 
 * Estrada-Martínez, F.J. Aguado-Delgado, J., Hilera, J.R., Otón, S., Gutiérrez-Martínez, J.M. (2018). Corrección de errores de accesibilidad en una aplicación móvil nativa: Caso de estudio. ATICA2018: Aplicación de Tecnologías de la Información y Comunicaciones Avanzadas y Accesibilidad, Universidad de Alcalá, pp. 86-92. [http://www.cc.uah.es/Atica/Atica2018/][atica2018]. 

También está disponible en este repositorio, en el archivo **CorreccionErrores.pdf**.

 ## Licencia
 <a rel="license" href="http://creativecommons.org/licenses/by-sa/4.0/"><img alt="Licencia Creative Commons" style="border-width:0" src="https://i.creativecommons.org/l/by-sa/4.0/88x31.png" /></a><br /><span xmlns:dct="http://purl.org/dc/terms/" property="dct:title">Ejemplo App Accesible Android</span> por <a xmlns:cc="http://creativecommons.org/ns#" href="www.cc.uah.es" property="cc:attributionName" rel="cc:attributionURL">Universidad de Alcalá, Departamento de Ciencias de la Computación</a> se distribuye bajo una <a rel="license" href="http://creativecommons.org/licenses/by-sa/4.0/">Licencia Creative Commons Atribución-CompartirIgual 4.0 Internacional</a>.