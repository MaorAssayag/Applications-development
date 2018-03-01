using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class factPanel : MonoBehaviour {
    bool state = false;
    public GameObject Panel;
    bool infoPressed = false;

    // Use this for initialization
    void Start () {
        Panel.SetActive(false);
    }
	
	// Update is called once per frame
	void Update () {	
	}

    public void OnClick()
    {
        if (!infoPressed) { infoPressed = !infoPressed; }
        state = !state;
        Panel.SetActive(state);
    }

    public void MenuPressed()
    {
       if (!state && !infoPressed)
        {
            OnClick();
        }
    }
}
