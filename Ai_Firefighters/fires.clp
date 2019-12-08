(deftemplate fire 
    (slot sender)
    (slot fireId)  
    (slot posX) 
    (slot posY)
    (slot isActive
        (default TRUE))
)

(deftemplate clean-up
    (slot fireId))
    
(deftemplate modify-fire
(slot fireId)
(slot posX) 
(slot posY) 
(slot isActive)

)

    
(defrule cleanup-for-fire
(clean-up (fireId ?id))
?t <- (modify-fire (fireId ?id))
 =>
 (retract ?t))
 
 
 
 (defquery get-fires
	"Queries the active fires"
	(modify-fire))
	


