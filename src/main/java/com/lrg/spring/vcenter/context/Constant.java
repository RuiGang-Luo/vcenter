package com.lrg.spring.vcenter.context;

public class Constant {
    public static final String SESSION_URL = "/rest/com/vmware/cis/session";
    public static final String SESSION_PARAM = "vmware-api-session-id";
    public static final String LIST_URL = "/rest/vcenter/vm";
    public static final String CLOSE_URL = "/rest/vcenter/vm/$targetId$/power/stop";
    public static final String START_URL = "/rest/vcenter/vm/$targetId$/power/start?";
    public static final String USERNAME = "";
    public static final String PASSWORD = "";//Basic YXBwbGVAdnNwaGVyZS5sb2NhbDpWTXdhcmUxIQ==

/*

{
    "type": "com.vmware.vapi.std.errors.unauthenticated",
    "value": {
        "error_type": "UNAUTHENTICATED",
        "messages": [
            {
                "args": [],
                "default_message": "Authentication required.",
                "id": "com.vmware.vapi.endpoint.method.authentication.required"
            }
        ],
        "challenge": "Basic realm=\"VAPI endpoint\",SIGN realm=e91864e263edd8fd4f05f76282fe10557521e152,service=\"VAPI endpoint\",sts=\"https://192.168.100.50/sts/STSService/vsphere.local\""
    }
}







{
    "value": [
        {
            "memory_size_MiB": 4096,
            "vm": "vm-2061",
            "name": "VM-DC01",
            "power_state": "POWERED_ON",
            "cpu_count": 4
        },
        {
            "memory_size_MiB": 4096,
            "vm": "vm-2063",
            "name": "NVIDIA-LS-100.56",
            "power_state": "POWERED_ON",
            "cpu_count": 2
        },
        {
            "memory_size_MiB": 28672,
            "vm": "vm-2087",
            "name": "VCSA02",
            "power_state": "POWERED_ON",
            "cpu_count": 8
        },
        {
            "memory_size_MiB": 8192,
            "vm": "vm-2101",
            "name": "winser2019",
            "power_state": "POWERED_OFF",
            "cpu_count": 4
        },
        {
            "memory_size_MiB": 8192,
            "vm": "vm-2102",
            "name": "VCS-100.53",
            "power_state": "POWERED_ON",
            "cpu_count": 4
        },
        {
            "memory_size_MiB": 8192,
            "vm": "vm-2103",
            "name": "COMP-100.54",
            "power_state": "POWERED_ON",
            "cpu_count": 4
        },
        {
            "memory_size_MiB": 8192,
            "vm": "vm-3002",
            "name": "citrix-sf-100.80",
            "power_state": "POWERED_ON",
            "cpu_count": 4
        },
        {
            "memory_size_MiB": 8192,
            "vm": "vm-3003",
            "name": "citrix-sql-100.81",
            "power_state": "POWERED_ON",
            "cpu_count": 4
        },
        {
            "memory_size_MiB": 8192,
            "vm": "vm-3004",
            "name": "KMS-100.57",
            "power_state": "POWERED_ON",
            "cpu_count": 4
        },
        {
            "memory_size_MiB": 8192,
            "vm": "vm-3005",
            "name": "citrix-ddc-100.82",
            "power_state": "POWERED_ON",
            "cpu_count": 4
        },
        {
            "memory_size_MiB": 8192,
            "vm": "vm-3006",
            "name": "citrix-pvs-100.83",
            "power_state": "POWERED_ON",
            "cpu_count": 4
        },
        {
            "memory_size_MiB": 6144,
            "vm": "vm-3015",
            "name": "pvs-win10-3",
            "power_state": "POWERED_OFF",
            "cpu_count": 4
        },
        {
            "memory_size_MiB": 6144,
            "vm": "vm-3016",
            "name": "pvs-win10-4",
            "power_state": "POWERED_ON",
            "cpu_count": 4
        },
        {
            "memory_size_MiB": 6144,
            "vm": "vm-3019",
            "name": "pvs-win10-1",
            "power_state": "POWERED_ON",
            "cpu_count": 4
        },
        {
            "memory_size_MiB": 8192,
            "vm": "vm-3021",
            "name": "citrix-licenses-100.85",
            "power_state": "POWERED_ON",
            "cpu_count": 4
        },
        {
            "memory_size_MiB": 8192,
            "vm": "vm-3022",
            "name": "citrix-wem-100.84",
            "power_state": "POWERED_ON",
            "cpu_count": 4
        },
        {
            "memory_size_MiB": 8192,
            "vm": "vm-4002",
            "name": "winser2019-vapp",
            "power_state": "POWERED_OFF",
            "cpu_count": 4
        },
        {
            "memory_size_MiB": 8192,
            "vm": "vm-4007",
            "name": "win2019-vapp1",
            "power_state": "POWERED_ON",
            "cpu_count": 4
        },
        {
            "memory_size_MiB": 20480,
            "vm": "vm-4010",
            "name": "CitrixHypervisor82-100.91",
            "power_state": "POWERED_ON",
            "cpu_count": 10
        },
        {
            "memory_size_MiB": 6144,
            "vm": "vm-4014",
            "name": "HYwin10-8q-1",
            "power_state": "POWERED_ON",
            "cpu_count": 4
        },
        {
            "memory_size_MiB": 2048,
            "vm": "vm-4015",
            "name": "NSVPX-100.90-92",
            "power_state": "POWERED_ON",
            "cpu_count": 2
        },
        {
            "memory_size_MiB": 8192,
            "vm": "vm-4017",
            "name": "HYwin10-8q-2",
            "power_state": "POWERED_ON",
            "cpu_count": 6
        },
        {
            "memory_size_MiB": 8192,
            "vm": "vm-4026",
            "name": "vmware-dem-100.58",
            "power_state": "POWERED_ON",
            "cpu_count": 4
        },
        {
            "memory_size_MiB": 4096,
            "vm": "vm-4030",
            "name": "AD",
            "power_state": "POWERED_ON",
            "cpu_count": 2
        },
        {
            "memory_size_MiB": 4096,
            "vm": "vm-4031",
            "name": "CS01",
            "power_state": "POWERED_ON",
            "cpu_count": 2
        },
        {
            "memory_size_MiB": 4096,
            "vm": "vm-4033",
            "name": "Win10Ent-LTSC2019-190123",
            "power_state": "POWERED_ON",
            "cpu_count": 4
        },
        {
            "memory_size_MiB": 8192,
            "vm": "vm-4036",
            "name": "LIC",
            "power_state": "POWERED_ON",
            "cpu_count": 4
        },
        {
            "memory_size_MiB": 4096,
            "vm": "vm-4038",
            "name": "Win10-bak",
            "power_state": "POWERED_OFF",
            "cpu_count": 4
        }
    ]
}
 */
}
