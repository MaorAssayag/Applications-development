using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class aboutPanel : MonoBehaviour {
    bool state = false;
    public GameObject Panel;

    void Start () {
        Panel.SetActive(false);
    }

    void Update () {
		
	}

    public void OnClick()
    {
        state = !state;
        Panel.SetActive(state);
    }
}
