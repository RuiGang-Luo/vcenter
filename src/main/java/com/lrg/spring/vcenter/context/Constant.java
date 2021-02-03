package com.lrg.spring.vcenter.context;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class Constant {
    public static final String DEFAULTCHARSET = "UTF-8";
    public static final String VM_LIST_STR ="[\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 4096,\n" +
            "            \"vm\": \"vm-2061\",\n" +
            "            \"name\": \"VM-DC01\",\n" +
            "            \"power_state\": \"POWERED_ON\",\n" +
            "            \"cpu_count\": 4\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 4096,\n" +
            "            \"vm\": \"vm-2063\",\n" +
            "            \"name\": \"NVIDIA-LS-100.56\",\n" +
            "            \"power_state\": \"POWERED_ON\",\n" +
            "            \"cpu_count\": 2\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 28672,\n" +
            "            \"vm\": \"vm-2087\",\n" +
            "            \"name\": \"VCSA02\",\n" +
            "            \"power_state\": \"POWERED_ON\",\n" +
            "            \"cpu_count\": 8\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 8192,\n" +
            "            \"vm\": \"vm-2101\",\n" +
            "            \"name\": \"winser2019\",\n" +
            "            \"power_state\": \"POWERED_OFF\",\n" +
            "            \"cpu_count\": 4\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 8192,\n" +
            "            \"vm\": \"vm-2102\",\n" +
            "            \"name\": \"VCS-100.53\",\n" +
            "            \"power_state\": \"POWERED_ON\",\n" +
            "            \"cpu_count\": 4\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 8192,\n" +
            "            \"vm\": \"vm-2103\",\n" +
            "            \"name\": \"COMP-100.54\",\n" +
            "            \"power_state\": \"POWERED_ON\",\n" +
            "            \"cpu_count\": 4\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 8192,\n" +
            "            \"vm\": \"vm-3002\",\n" +
            "            \"name\": \"citrix-sf-100.80\",\n" +
            "            \"power_state\": \"POWERED_ON\",\n" +
            "            \"cpu_count\": 4\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 8192,\n" +
            "            \"vm\": \"vm-3003\",\n" +
            "            \"name\": \"citrix-sql-100.81\",\n" +
            "            \"power_state\": \"POWERED_ON\",\n" +
            "            \"cpu_count\": 4\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 8192,\n" +
            "            \"vm\": \"vm-3004\",\n" +
            "            \"name\": \"KMS-100.57\",\n" +
            "            \"power_state\": \"POWERED_ON\",\n" +
            "            \"cpu_count\": 4\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 8192,\n" +
            "            \"vm\": \"vm-3005\",\n" +
            "            \"name\": \"citrix-ddc-100.82\",\n" +
            "            \"power_state\": \"POWERED_ON\",\n" +
            "            \"cpu_count\": 4\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 8192,\n" +
            "            \"vm\": \"vm-3006\",\n" +
            "            \"name\": \"citrix-pvs-100.83\",\n" +
            "            \"power_state\": \"POWERED_ON\",\n" +
            "            \"cpu_count\": 4\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 6144,\n" +
            "            \"vm\": \"vm-3015\",\n" +
            "            \"name\": \"pvs-win10-3\",\n" +
            "            \"power_state\": \"POWERED_OFF\",\n" +
            "            \"cpu_count\": 4\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 6144,\n" +
            "            \"vm\": \"vm-3016\",\n" +
            "            \"name\": \"pvs-win10-4\",\n" +
            "            \"power_state\": \"POWERED_ON\",\n" +
            "            \"cpu_count\": 4\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 6144,\n" +
            "            \"vm\": \"vm-3019\",\n" +
            "            \"name\": \"pvs-win10-1\",\n" +
            "            \"power_state\": \"POWERED_ON\",\n" +
            "            \"cpu_count\": 4\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 8192,\n" +
            "            \"vm\": \"vm-3021\",\n" +
            "            \"name\": \"citrix-licenses-100.85\",\n" +
            "            \"power_state\": \"POWERED_ON\",\n" +
            "            \"cpu_count\": 4\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 8192,\n" +
            "            \"vm\": \"vm-3022\",\n" +
            "            \"name\": \"citrix-wem-100.84\",\n" +
            "            \"power_state\": \"POWERED_ON\",\n" +
            "            \"cpu_count\": 4\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 8192,\n" +
            "            \"vm\": \"vm-4002\",\n" +
            "            \"name\": \"winser2019-vapp\",\n" +
            "            \"power_state\": \"POWERED_OFF\",\n" +
            "            \"cpu_count\": 4\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 8192,\n" +
            "            \"vm\": \"vm-4007\",\n" +
            "            \"name\": \"win2019-vapp1\",\n" +
            "            \"power_state\": \"POWERED_ON\",\n" +
            "            \"cpu_count\": 4\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 20480,\n" +
            "            \"vm\": \"vm-4010\",\n" +
            "            \"name\": \"CitrixHypervisor82-100.91\",\n" +
            "            \"power_state\": \"POWERED_ON\",\n" +
            "            \"cpu_count\": 10\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 6144,\n" +
            "            \"vm\": \"vm-4014\",\n" +
            "            \"name\": \"HYwin10-8q-1\",\n" +
            "            \"power_state\": \"POWERED_ON\",\n" +
            "            \"cpu_count\": 4\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 2048,\n" +
            "            \"vm\": \"vm-4015\",\n" +
            "            \"name\": \"NSVPX-100.90-92\",\n" +
            "            \"power_state\": \"POWERED_ON\",\n" +
            "            \"cpu_count\": 2\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 8192,\n" +
            "            \"vm\": \"vm-4017\",\n" +
            "            \"name\": \"HYwin10-8q-2\",\n" +
            "            \"power_state\": \"POWERED_ON\",\n" +
            "            \"cpu_count\": 6\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 8192,\n" +
            "            \"vm\": \"vm-4026\",\n" +
            "            \"name\": \"vmware-dem-100.58\",\n" +
            "            \"power_state\": \"POWERED_ON\",\n" +
            "            \"cpu_count\": 4\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 4096,\n" +
            "            \"vm\": \"vm-4030\",\n" +
            "            \"name\": \"AD\",\n" +
            "            \"power_state\": \"POWERED_ON\",\n" +
            "            \"cpu_count\": 2\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 4096,\n" +
            "            \"vm\": \"vm-4031\",\n" +
            "            \"name\": \"CS01\",\n" +
            "            \"power_state\": \"POWERED_ON\",\n" +
            "            \"cpu_count\": 2\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 4096,\n" +
            "            \"vm\": \"vm-4033\",\n" +
            "            \"name\": \"Win10Ent-LTSC2019-190123\",\n" +
            "            \"power_state\": \"POWERED_ON\",\n" +
            "            \"cpu_count\": 4\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 8192,\n" +
            "            \"vm\": \"vm-4036\",\n" +
            "            \"name\": \"LIC\",\n" +
            "            \"power_state\": \"POWERED_ON\",\n" +
            "            \"cpu_count\": 4\n" +
            "        },\n" +
            "        {\n" +
            "            \"memory_size_MiB\": 4096,\n" +
            "            \"vm\": \"vm-4038\",\n" +
            "            \"name\": \"Win10-bak\",\n" +
            "            \"power_state\": \"POWERED_OFF\",\n" +
            "            \"cpu_count\": 4\n" +
            "        }\n" +
            "    ]";

}
