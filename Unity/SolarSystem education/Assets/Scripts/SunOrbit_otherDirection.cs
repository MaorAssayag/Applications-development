using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SunOrbit_otherDirection : MonoBehaviour {
    public float rotspeed = 0.1f;

    // Use this for initialization
    void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
        transform.Rotate(-transform.up, rotspeed, Space.World);
    }
}
