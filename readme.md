In dem beigefügten Eclipse-Projekt befindet sich die Thread-sichere einfache Implementierung einer ``doppelt verketteten Liste.`` Modifizieren Sie die Implementierung so, dass [Hand-over-Hand Locking](https://docs.google.com/presentation/d/125zeApr_1rNaVLFZJDZjZY0BYV_Q3AOIHTjL88yvdGo/edit#slide=id.g2a63e3b2f3_0_31) verwendet wird, um **``Thread-Sicherheit zu erreichen, damit nicht immer die gesamte Liste gesperrt ist.``**

Bei der Methode ``get`` ist zu beachten, dass immer die Methode ``inspect``, für die im Interface ``NewList`` eine default-Implementierung vorhanden ist, vor der Rückgabe des gefundenen Elements aufzurufen ist. *Dies dient hier nur der Verzögerung.*

Der Name, das zu implementierende Interface und das Paket der Implementierungsklasse dürfen nicht geändert werden.

Geben Sie die Java-Datei mit Ihrer Hand-Over-Hand gelockten NewList-Implementierung hier über Moodle ab. 
Die abzugebene Klasse soll ``NewListImpl`` heißen.
